package ferwafa.dao.interfc;

import java.util.List;
import ferwafa.domain.RegistrationRequest;

public interface IRegistrationRequest {
	public RegistrationRequest saveRequest(RegistrationRequest request);

	public List<RegistrationRequest> getListRequest();

	public RegistrationRequest getRequestById(int requestId, String primaryKeyclomunName);

	public RegistrationRequest UpdateRequest(RegistrationRequest request);
	public RegistrationRequest DeleteRequest(RegistrationRequest request);
	public String myNane();

	public RegistrationRequest getRequestWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
