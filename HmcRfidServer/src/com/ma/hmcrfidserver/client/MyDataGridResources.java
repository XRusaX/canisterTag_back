package com.ma.hmcrfidserver.client;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;
import com.google.gwt.user.cellview.client.DataGrid;

public interface MyDataGridResources extends DataGrid.Resources {
	public interface DataGridStyle extends DataGrid.Style {
	};
	
	@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
	@Source("/resources/fonts/OpenSans-Regular.ttf")
	DataResource openSansRegularTtf(); 
	
	@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
	@Source("/resources/fonts/Montserrat-Regular.ttf")
	DataResource montserratRegularTtf(); 

	@MimeType("application/font-sfnt") // use appropriate mime type depending on font file format
	@Source("/resources/fonts/Roboto-Regular.ttf")
	DataResource robotoRegularTtf(); 
	
	@Override
	@Source({ "/resources/css/MyDataGrid.css" })
	DataGridStyle dataGridStyle();
};
