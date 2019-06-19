/*
 * Axelor Business Solutions
 * 
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.project.db;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT_STATUS", indexes = { @Index(columnList = "name") })
public class ProjectStatus extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_STATUS_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_STATUS_SEQ", sequenceName = "PROJECT_PROJECT_STATUS_SEQ", allocationSize = 1)
	private Long id;

	private String name;

	private Boolean defaultStatus = Boolean.FALSE;

	private Boolean isOpen = Boolean.FALSE;

	private Boolean isClose = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectStatus() {
	}

	public ProjectStatus(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDefaultStatus() {
		return defaultStatus == null ? Boolean.FALSE : defaultStatus;
	}

	public void setDefaultStatus(Boolean defaultStatus) {
		this.defaultStatus = defaultStatus;
	}

	public Boolean getIsOpen() {
		return isOpen == null ? Boolean.FALSE : isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Boolean getIsClose() {
		return isClose == null ? Boolean.FALSE : isClose;
	}

	public void setIsClose(Boolean isClose) {
		this.isClose = isClose;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof ProjectStatus)) return false;

		final ProjectStatus other = (ProjectStatus) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("name", getName())
			.add("defaultStatus", getDefaultStatus())
			.add("isOpen", getIsOpen())
			.add("isClose", getIsClose())
			.omitNullValues()
			.toString();
	}
}
