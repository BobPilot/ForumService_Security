package telran.forum.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.configuration.AccountUserCredential;
import telran.forum.dao.ForumRepository;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.Comment;
import telran.forum.domain.Post;
import telran.forum.domain.UserAccount;
import telran.forum.dto.DatePeriodDto;
import telran.forum.dto.NewCommentDto;
import telran.forum.dto.NewPostDto;
import telran.forum.dto.PostUpdateDto;

@Service
public class ForumServiceImpl implements ForumService {

	@Autowired
	ForumRepository repository;
	
	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public Post addNewPost(NewPostDto newPost) {
		Post post = convertToPost(newPost);
		repository.save(post);
		return post;
	}

	private Post convertToPost(NewPostDto newPost) {
		return new Post(newPost.getTitle(), newPost.getContent(), newPost.getAuthor(), newPost.getTags());
	}

	@Override
	public Post getPost(String id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Post removePost(String id, String auth) {
		Post post = repository.findById(id).orElse(null);
		if (post != null) {
			AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
			UserAccount user = userRepository.findById(credentials.getLogin()).get();
			Set<String> roles = user.getRoles();
			boolean hasRight = roles.stream()
					.anyMatch(s -> "Admin".equals(s) || "Moderator".equals(s));
			hasRight = hasRight || credentials.getLogin().equals(post.getAuthor());
			if (!hasRight) {
				throw new ForbiddenException();
			}
			repository.delete(post);
		}
		return post;
	}

	@Override
	public Post updatePost(PostUpdateDto updatePost, String auth) {
		Post post = repository.findById(updatePost.getId()).orElse(null);
		if (post != null) {
			AccountUserCredential credentials = accountConfiguration.tokenDecode(auth);
			if (!credentials.getLogin().equals(post.getAuthor())) {
				throw new ForbiddenException();
			}
			post.setContent(updatePost.getContent());
			repository.save(post);
		}
		return post;
	}

	@Override
	public boolean addLike(String id) {
		Post post = repository.findById(id).orElse(null);
		if (post != null) {
			post.addLike();
			repository.save(post);
			return true;
		}
		return false;
	}

	@Override
	public Post addComment(String id, NewCommentDto newComment) {
		Post post = repository.findById(id).orElse(null);
		if (post != null) {
			Comment comment = new Comment(newComment.getUser(), newComment.getMessage());
			post.addComment(comment);
			repository.save(post);
		}
		return post;
	}

	@Override
	public Iterable<Post> findByTags(List<String> tags) {
		return repository.findByTagsIn(tags);
	}

	@Override
	public Iterable<Post> findByAuthor(String author) {
		return repository.findByAuthor(author);
	}

	@Override
	public Iterable<Post> findByDate(DatePeriodDto period) {
		return repository.findByDateCreatedBetween(LocalDate.parse(period.getFrom()), LocalDate.parse(period.getTo()));
	}

}
