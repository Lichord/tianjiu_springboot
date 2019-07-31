package bjfu.lixian.service;

import bjfu.lixian.pojo.Users;

public interface UsersService {
    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
    public boolean queryUsernameIsExist(String name);
    public void saveUser(Users user);
    public Users queryUserIsExist(String username,String password);
    public void updateUserInfo(Users user);
    public Users queryUserInfo(String userId);
}
