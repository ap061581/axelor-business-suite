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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT_FOLDER", indexes = { @Index(columnList = "name") })
public class ProjectFolder extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_FOLDER_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_FOLDER_SEQ", sequenceName = "PROJECT_PROJECT_FOLDER_SEQ", allocationSize = 1)
	private Long id;

	private String name;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "projectFolderSet", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> projectSet;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectFolder() {
	}

	public ProjectFolder(String name) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Project> getProjectSet() {
		return projectSet;
	}

	public void setProjectSet(Set<Project> projectSet) {
		this.projectSet = projectSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code projectSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			setProjectSet(new HashSet<>());
		}
		getProjectSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code projectSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			return;
		}
		getProjectSet().remove(item);
	}

	/**
	 * Clear the {@code projectSet} collection.
	 *
	 */
	public void clearProjectSet() {
		if (getProjectSet() != null) {
			getProjectSet().clear();
		}
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
		if (!(obj instanceof ProjectFolder)) return false;

		final ProjectFolder other = (ProjectFolder) obj;
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
			.omitNullValues()
			.toString();
	}
}
