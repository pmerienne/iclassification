package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageLabel implements Serializable {

	private static final long serialVersionUID = 6420559226369690123L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String name;

	@Indexed(unique = true)
	private String shortCode;

	private String description;

	private String thumbnail;

	public ImageLabel() {
		super();
	}

	public ImageLabel(String name, String shortCode) {
		super();
		this.name = name;
		this.shortCode = shortCode;
	}

	public ImageLabel(String name, String shortCode, String description, String thumbnail) {
		super();
		this.name = name;
		this.shortCode = shortCode;
		this.description = description;
		this.thumbnail = thumbnail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((shortCode == null) ? 0 : shortCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageLabel other = (ImageLabel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shortCode == null) {
			if (other.shortCode != null)
				return false;
		} else if (!shortCode.equals(other.shortCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageLabel [id=" + id + ", name=" + name + ", shortCode=" + shortCode + "]";
	}

}
