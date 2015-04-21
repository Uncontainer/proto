package com.yeon.infra.remote.table;

public class Column {
	public Column(String name) {
		this.name = name;
	}

	public Column(String name, int width, String align) {
		super();
		this.name = name;
		this.width = width;
		this.align = align;
	}

	String name;
	int width;
	String align;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getHeaderCellStyle() {
		String style = "";
		if (width > 0) {
			style += "width:" + width + "px;";
		}

		return style;
	}

	public String getBodyCellStyle() {
		String style = "";
		if (align != null) {
			style += "text-align:" + align + ";";
		}

		return style;
	}
}
