package com.pmerienne.iclassification.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DB;
import com.pmerienne.iclassification.server.repository.ImageLabelRepository;
import com.pmerienne.iclassification.server.repository.WorkspaceRepository;
import com.pmerienne.iclassification.server.service.ImageService;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public abstract class IntegrationTest {

	private List<String> SYSTEM_COLLECTIONS = Arrays.asList("fs.chunks", "fs.files", "system.indexes", "system.users");

	private final static File ZIP_FILE = new File("src/test/resources/data/dataset-flowers.min.zip");

	@Autowired
	private ImageService imageService;

	@Autowired
	private WorkspaceRepository workspaceRepository;

	@Autowired
	private ImageLabelRepository imageLabelRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	protected Workspace testWorkspace;

	protected ImageLabel bluebell;
	protected ImageLabel crocus;
	protected ImageLabel snowdrop;

	@Before
	public void setup() {
		this.cleanDB();

		this.testWorkspace = new Workspace("Test workspace");
		this.workspaceRepository.save(testWorkspace);

		this.bluebell = new ImageLabel("bluebell", "bluebell");
		this.crocus = new ImageLabel("crocus", "crocus");
		this.snowdrop = new ImageLabel("snowdrop", "snowdrop");
		this.imageLabelRepository.save(Arrays.asList(bluebell, crocus, snowdrop));
	}

	@After
	public void tearDown() {
		this.cleanDB();
	}

	protected void cleanDB() {
		DB db = this.mongoTemplate.getDb();
		Set<String> collections = db.getCollectionNames();
		for (String collection : collections) {
			if (!SYSTEM_COLLECTIONS.contains(collection)) {
				this.mongoTemplate.remove(new Query(), collection);
			}
		}
	}

	/**
	 * Import some image file :
	 * 
	 * <pre>
	 * ImageFile expectedEgret1 = new ImageFile(&quot;3777402756&quot;, ImageLabel.EGRET);
	 * ImageFile expectedEgret2 = new ImageFile(&quot;4239281284&quot;, ImageLabel.EGRET);
	 * ImageFile expectedOwl1 = new ImageFile(&quot;791682407&quot;, ImageLabel.OWL);
	 * ImageFile expectedOwl2 = new ImageFile(&quot;3743525590&quot;, ImageLabel.OWL);
	 * ImageFile expectedMandarin1 = new ImageFile(&quot;540609095&quot;, ImageLabel.MANDARIN);
	 * ImageFile expectedMandarin2 = new ImageFile(&quot;3655119658&quot;, ImageLabel.MANDARIN);
	 * </pre>
	 */
	protected List<ImageMetadata> loadTestData() {
		try {
			return this.imageService.importFromZip(this.testWorkspace, new FileInputStream(ZIP_FILE));
		} catch (Exception ex) {
			throw new RuntimeException("Unable to load image files", ex);
		}
	}
}
