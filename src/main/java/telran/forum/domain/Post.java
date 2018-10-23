package telran.forum.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Forum")
public class Post {
	@Id
	String id;
	String title;
	String content;
	String author;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	LocalDateTime dateCreated;
	Set<String> tags;
	int likes;
	Set<Comment> comments;

	public Post() {
	}

	public Post(String title, String content, String author, Set<String> tags) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.tags = tags;
		comments = new HashSet<>();
		dateCreated = LocalDateTime.now();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public Set<String> getTags() {
		return tags;
	}

	public int getLikes() {
		return likes;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void addLike() {
		likes++;
	}

	public boolean addComment(Comment comment) {
		return comments.add(comment);
	}

	public boolean addTag(String tag) {
		return tags.add(tag);
	}

	public boolean removeTag(String tag) {
		return tags.remove(tag);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Post)) {
			return false;
		}
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	
}
