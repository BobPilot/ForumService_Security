package telran.forum.service;

import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserProfileRolesDto;
import telran.forum.dto.UserRegisterDto;

public interface AccountService {

	public UserProfileDto addUser(UserRegisterDto userRegDto, String auth);

	public UserProfileDto editUser(UserRegisterDto userRegDto, String auth);

	public UserProfileDto removeUser(String id, String auth);

    public UserProfileRolesDto setModerator(String id);

    public UserProfileDto changePassword(String id, String auth, String upd);



}
