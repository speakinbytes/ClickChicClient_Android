package com.speakinbytes.login.models;

public class PostModel {

	private String comentario;

	private String tipology;
	private String ctvi;
	
	private String app_entity;
	
	private String hashTags;
	
	public String getTipology() {
		return tipology;
	}
	public void setTipology(String tipology) {
		this.tipology = tipology;
	}
	public String getCtvi() {
		return ctvi;
	}
	public void setCtvi(String ctvi) {
		this.ctvi = ctvi;
	}
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getApp_entity() {
		return app_entity;
	}
	public void setApp_entity(String app_entity) {
		this.app_entity = app_entity;
	}
	public String getHashTags() {
		return hashTags;
	}
	public void setHashTags(String hashTags) {
		this.hashTags = hashTags;
	}
}
