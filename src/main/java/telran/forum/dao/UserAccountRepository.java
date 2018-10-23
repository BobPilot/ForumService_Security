package telran.forum.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;
import telran.forum.domain.UserAccount;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

}
