package com.pmerienne.iclassification.server.service;

import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface DatasetService {

	Dataset createDataset(Workspace workspace, int percent);
}
