package com.pmerienne.iclassification.server.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.shared.model.Workspace;

public class WorkspaceRepositoryTest extends IntegrationTest {

	@Autowired
	private WorkspaceRepository workspaceRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Test
	public void testFindAll() {
		List<Workspace> actualWorkspaces = this.workspaceRepository.findAll();
		assertTrue(actualWorkspaces.get(0).equals(this.testWorkspace));
	}
}
