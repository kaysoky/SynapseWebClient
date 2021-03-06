package org.sagebionetworks.web.client.mvp;

import org.sagebionetworks.web.client.place.Challenges;
import org.sagebionetworks.web.client.place.ComingSoon;
import org.sagebionetworks.web.client.place.Down;
import org.sagebionetworks.web.client.place.Governance;
import org.sagebionetworks.web.client.place.Home;
import org.sagebionetworks.web.client.place.LoginPlace;
import org.sagebionetworks.web.client.place.Profile;
import org.sagebionetworks.web.client.place.ProjectsHome;
import org.sagebionetworks.web.client.place.Search;
import org.sagebionetworks.web.client.place.Settings;
import org.sagebionetworks.web.client.place.Synapse;
import org.sagebionetworks.web.client.place.Team;
import org.sagebionetworks.web.client.place.TeamSearch;
import org.sagebionetworks.web.client.place.Wiki;
import org.sagebionetworks.web.client.place.WikiPlace;
import org.sagebionetworks.web.client.place.users.PasswordReset;
import org.sagebionetworks.web.client.place.users.RegisterAccount;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers({ Home.Tokenizer.class, LoginPlace.Tokenizer.class,
		PasswordReset.Tokenizer.class, RegisterAccount.Tokenizer.class,
		ProjectsHome.Tokenizer.class, Profile.Tokenizer.class,
		ComingSoon.Tokenizer.class, Synapse.Tokenizer.class, Wiki.Tokenizer.class,
		Search.Tokenizer.class, Settings.Tokenizer.class,
		Challenges.Tokenizer.class, Governance.Tokenizer.class,
		WikiPlace.Tokenizer.class, Down.Tokenizer.class, Team.Tokenizer.class, 
		TeamSearch.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
