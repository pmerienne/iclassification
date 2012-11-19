package com.pmerienne.iclassification.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ LoginPlace.Tokenizer.class, WorkspaceConfigurationPlace.Tokenizer.class, ImagesPlace.Tokenizer.class, ImageLabelsPlace.Tokenizer.class,
		ImageDetailPlace.Tokenizer.class })
public interface ApplicationPlaceHistoryMapper extends PlaceHistoryMapper {
}