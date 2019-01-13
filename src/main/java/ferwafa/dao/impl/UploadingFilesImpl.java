/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IUploadingFiles;
import ferwafa.domain.UploadingTeamLogo;
/**
 *
 * @author Emmanuel
 */

public class UploadingFilesImpl extends AbstractDao<Long, UploadingTeamLogo> implements IUploadingFiles {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public UploadingTeamLogo saveUploadedFile(UploadingTeamLogo upload) {
		return saveIntable(upload);
	}

	@SuppressWarnings("unchecked")
	public List<UploadingTeamLogo> getListUploadedFiles() {
		return (List<UploadingTeamLogo>) (Object) getModelList();
	}

	public UploadingTeamLogo getUploadedFileById(int UploadId, String primaryKeyclomunName) {
		return (UploadingTeamLogo) getModelById(UploadId, primaryKeyclomunName);
	}

	public UploadingTeamLogo UpdateUploadedFile(UploadingTeamLogo upload) {
		return updateIntable(upload);
	}

	public String myName() {
		// TODO Auto-generated method stub
		return "Emma";
	}

	public UploadingTeamLogo getUploadedFilesWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (UploadingTeamLogo) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}
