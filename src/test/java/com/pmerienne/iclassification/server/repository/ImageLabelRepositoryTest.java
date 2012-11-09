package com.pmerienne.iclassification.server.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ImageLabelRepositoryTest extends IntegrationTest {

	@Autowired
	private ImageLabelRepository imageLabelRepository;

	@Test
	public void testSave() {
		ImageLabel label = new ImageLabel("test", "test");
		this.imageLabelRepository.save(label);

		ImageLabel actualLabel = this.imageLabelRepository.findOne(label.getId());
		assertEquals(label, actualLabel);
	}

	@Test(expected = DuplicateKeyException.class)
	public void testSaveWithDuplicateName() {
		ImageLabel label1 = new ImageLabel("duplicate", "test1");
		this.imageLabelRepository.save(label1);
		ImageLabel label2 = new ImageLabel("duplicate", "test2");
		this.imageLabelRepository.save(label2);
	}

	@Test(expected = DuplicateKeyException.class)
	public void testSaveWithDuplicateShortCode() {
		ImageLabel label1 = new ImageLabel("duplicate1", "test");
		this.imageLabelRepository.save(label1);
		ImageLabel label2 = new ImageLabel("duplicate2", "test");
		this.imageLabelRepository.save(label2);
	}
}
