package org.sagebionetworks.web.client.widget.sharing;

import org.sagebionetworks.web.client.IconsImageBundle;
import org.sagebionetworks.web.client.widget.sharing.AccessMenuButton.AccessLevel;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AccessMenuButtonViewImpl extends LayoutContainer implements AccessMenuButtonView {

	private Presenter presenter;
	private IconsImageBundle iconsImageBundle;	
	private Button button;
	private AccessLevel accessLevel;
	
	private static final String buttonPrefix = "Access: ";
	
	@Inject
	public AccessMenuButtonViewImpl(IconsImageBundle iconsImageBundle) {
		this.iconsImageBundle = iconsImageBundle;		
		button = new Button();					
		button.setIconAlign(IconAlign.LEFT);			
		button.setMenu(createAccessMenu());
		button.setHeight(25);					
	}
	
	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);		
		add(button);
	}
	
	@Override
	public void setAccessLevel(AccessLevel level) {
		this.accessLevel = level;

		String levelText = "";
		ImageResource icon = null;
		switch(level) {
		case PUBLIC:
			levelText = "Public";
			icon = iconsImageBundle.lockUnlocked16();
			break;
		case PRIVATE:
			levelText = "Private";
			icon = iconsImageBundle.lock16();
			break;
		case SHARED:
			levelText = "Shared";
			icon = iconsImageBundle.lock16();
			break;
		}
		button.setText(buttonPrefix + levelText);
		button.setIcon(AbstractImagePrototype.create(icon));		
	}	
	
	@Override
	public Widget asWidget() {
		return this;
	}
	

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*
	 * Private Methods
	 */
	private Menu createAccessMenu() {
		Menu menu = new Menu();		
		MenuItem item = null; 
			
		item = new MenuItem("Sharing Settings...");
		item.setIcon(AbstractImagePrototype.create(iconsImageBundle.users16()));
		menu.add(item);
				
		return menu;
	}

}