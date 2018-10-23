package telran.forum.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Comment {
	String user;
	String message;
	@JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
	LocalDateTime dateCreated;
	int likes;
	
	public Comment() {
	}

	public Comment(String user, String message) {
		this.user = user;
		this.message = message;
		dateCreated = LocalDateTime.now();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public int getLikes() {
		return likes;
	}
	
	public void addLike() {
		likes++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (!(obj instanceof Comment)) {
			return false;
		}
		Comment other = (Comment) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null) {
				return false;
			}
		} else if (!dateCreated.equals(other.dateCreated)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

}
