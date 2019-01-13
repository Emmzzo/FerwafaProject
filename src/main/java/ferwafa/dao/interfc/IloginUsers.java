package ferwafa.dao.interfc;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

import ferwafa.domain.Users;

/**
 *
 * @author RTAP4
 */
public interface IloginUsers {
   public boolean  checkUserNameAndPasswod(String userName,String Password);
   public Users userDetail(String userName);
   public String criptPassword(String password)throws NoSuchAlgorithmException; 
   public String getIpAddress()throws Exception;
}
