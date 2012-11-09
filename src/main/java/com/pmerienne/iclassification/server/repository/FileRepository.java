package com.pmerienne.iclassification.server.repository;

import java.io.File;
import java.util.List;

public interface FileRepository {

	String save(File file);

	void delete(String filename);

	void deleteAll();

	File get(String filename);

	boolean contains(String filename);

	List<String> findAll();

}