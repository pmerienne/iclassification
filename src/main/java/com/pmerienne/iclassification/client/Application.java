package com.pmerienne.iclassification.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.pmerienne.iclassification.client.activity.ApplicationActivityMapper;
import com.pmerienne.iclassification.client.event.WorkspaceLoadedEvent;
import com.pmerienne.iclassification.client.event.WorkspaceLoadedHandler;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.factory.ClientFactoryImpl;
import com.pmerienne.iclassification.client.place.ApplicationPlaceHistoryMapper;
import com.pmerienne.iclassification.client.place.LoginPlace;
import com.pmerienne.iclassification.client.resource.JsLibraries;
import com.pmerienne.iclassification.client.utils.JavascriptInjector;
import com.pmerienne.iclassification.client.view.ApplicationContainer;
import com.pmerienne.iclassification.shared.model.Workspace;

public class Application implements EntryPoint {

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				// Window.alert("uncaught: " + e.getMessage());
				e.printStackTrace();
			}
		});

		new Timer() {
			@Override
			public void run() {
				createApplication();
			}

		}.schedule(1);

	}

	private void createApplication() {
		// Init display
		final ApplicationContainer appContainer = new ApplicationContainer();
		Place defaultPlace = new LoginPlace();

		// Init client factory
		ClientFactory clientFactory = new ClientFactoryImpl();
		final EventBus eventBus = clientFactory.getEventBus();
		final PlaceController placeController = clientFactory.getPlaceController();

		// Start ActivityManager for the main widget with our ActivityMapper
		final ActivityMapper activityMapper = new ApplicationActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		activityManager.setDisplay(appContainer.getContent());

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		final ApplicationPlaceHistoryMapper historyMapper = GWT.create(ApplicationPlaceHistoryMapper.class);
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		RootPanel.get().add(appContainer);

		// Goes to the place represented on URL else default place
		historyHandler.handleCurrentHistory();

		// Bind view container
		eventBus.addHandler(WorkspaceLoadedEvent.getType(), new WorkspaceLoadedHandler() {
			@Override
			public void onWorkspaceLoaded(WorkspaceLoadedEvent event) {
				Workspace workspace = event.getWorkspace();
				appContainer.setWorkspace(workspace);
			}
		});

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				Place place = historyMapper.getPlace(historyToken);
				if(place != null) {
					appContainer.setPlace(place.getClass());
				}
			}
		});
		
		// Inject js library
		JsLibraries jsLibraries = JsLibraries.INSTANCE;
		JavascriptInjector.inject(jsLibraries.jCrop().getText());
		JavascriptInjector.inject(jsLibraries.toastr().getText());
	}

}
