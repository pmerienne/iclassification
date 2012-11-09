package com.pmerienne.iclassification.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ImageLabelsPlace extends Place {

	public static class Tokenizer implements PlaceTokenizer<ImageLabelsPlace> {

		@Override
		public ImageLabelsPlace getPlace(String token) {
			return new ImageLabelsPlace();
		}

		@Override
		public String getToken(ImageLabelsPlace place) {
			return "";
		}

	}
}
