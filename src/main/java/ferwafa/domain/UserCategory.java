package ferwafa.domain;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
*
* @author Emmanuel
*/
@Entity
@Table(name = "UserCategory")
@NamedQuery(name = "UserCategory.findAll",
        query = "SELECT r FROM UserCategory r order by userCatid desc")
public class UserCategory  extends CommonDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "userCatid")
    private int userCatid;

    @Column(name = "usercategoryName")
    private String usercategoryName;

    public int getUserCatid() {
        return userCatid;
    }

    public void setUserCatid(int userCatid) {
        this.userCatid = userCatid;
    }

    public String getUsercategoryName() {
        return usercategoryName;
    }

    public void setUsercategoryName(String usercategoryName) {
        this.usercategoryName = usercategoryName;
    }

	

}
