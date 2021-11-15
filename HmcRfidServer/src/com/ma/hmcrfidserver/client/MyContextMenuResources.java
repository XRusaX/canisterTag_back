package com.ma.hmcrfidserver.client;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;

public interface MyContextMenuResources extends CellList.Resources{
		public interface MenuStyle extends Style{}
		
//		@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
//		@Source("/resources/fonts/OpenSans-Regular.ttf")
//		DataResource openSansRegularTtf(); 
//		
//		@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
//		@Source("/resources/fonts/Montserrat-Regular.ttf")
//		DataResource montserratRegularTtf(); 
//
//		@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
//		@Source("/resources/fonts/Roboto-Regular.ttf")
//		DataResource robotoRegularTtf(); 
//		
		@Override
		@Source({ "/resources/css/Menu.css" })
		MenuStyle cellListStyle();
//		
//		@Source("/resources/css/Custom.css")
//	    @CssResource.NotStrict
//	    CssResource customization();
}
