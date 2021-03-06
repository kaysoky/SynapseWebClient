package org.sagebionetworks.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class Profile extends Place{
	
	private String token;
	public static final String EDIT_PROFILE_PLACE_TOKEN = "e";
	public static final String VIEW_PROFILE_PLACE_TOKEN = "v";

	public Profile(String token) {
		this.token = token;
	}

	public String toToken() {
		return token;
	}
	
	@Prefix("!Profile")
	public static class Tokenizer implements PlaceTokenizer<Profile> {
        @Override
        public String getToken(Profile place) {
            return place.toToken();
        }

        @Override
        public Profile getPlace(String token) {
            return new Profile(token);
        }
    }

}
