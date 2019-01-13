/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ferwafa.dao.interfc;
import java.util.List;
import ferwafa.domain.UploadingTeamLogo;

/**
 *
 * @author Emmanuel
 */
public interface IUploadingFiles {
	public UploadingTeamLogo saveUploadedFile(UploadingTeamLogo upload);

	public List<UploadingTeamLogo> getListUploadedFiles();

	public UploadingTeamLogo getUploadedFileById(int UploadId, String primaryKeyclomunName);

	public UploadingTeamLogo UpdateUploadedFile(UploadingTeamLogo upload);

	public String myName();

	public UploadingTeamLogo getUploadedFilesWithQuery(final String[] propertyName, final Object[] value,
			final String hqlStatement);
}