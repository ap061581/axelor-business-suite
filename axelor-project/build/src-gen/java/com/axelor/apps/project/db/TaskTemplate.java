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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.team.db.Team;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_TASK_TEMPLATE", indexes = { @Index(columnList = "name"), @Index(columnList = "parent_task_template"), @Index(columnList = "assigned_to"), @Index(columnList = "team") })
public class TaskTemplate extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_TASK_TEMPLATE_SEQ")
	@SequenceGenerator(name = "PROJECT_TASK_TEMPLATE_SEQ", sequenceName = "PROJECT_TASK_TEMPLATE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Name", search = { "id", "name" })
	@NameColumn
	private String name;

	@Widget(title = "Delay to start (Hours)")
	private BigDecimal delayToStart = new BigDecimal("0");

	@Widget(title = "Duration (Hours)")
	private BigDecimal duration = BigDecimal.ZERO;

	@Widget(title = "Total planned hours")
	private BigDecimal totalPlannedHrs = BigDecimal.ZERO;

	@Widget(title = "Parent task")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TaskTemplate parentTaskTemplate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTaskTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskTemplate> taskTemplateList;

	@Widget(title = "Assigned to")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedTo;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	private Boolean isUniqueTaskForMultipleQuantity = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public TaskTemplate() {
	}

	public TaskTemplate(String name) {
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

	public BigDecimal getDelayToStart() {
		return delayToStart == null ? BigDecimal.ZERO : delayToStart;
	}

	public void setDelayToStart(BigDecimal delayToStart) {
		this.delayToStart = delayToStart;
	}

	public BigDecimal getDuration() {
		return duration == null ? BigDecimal.ZERO : duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public BigDecimal getTotalPlannedHrs() {
		return totalPlannedHrs == null ? BigDecimal.ZERO : totalPlannedHrs;
	}

	public void setTotalPlannedHrs(BigDecimal totalPlannedHrs) {
		this.totalPlannedHrs = totalPlannedHrs;
	}

	public TaskTemplate getParentTaskTemplate() {
		return parentTaskTemplate;
	}

	public void setParentTaskTemplate(TaskTemplate parentTaskTemplate) {
		this.parentTaskTemplate = parentTaskTemplate;
	}

	public List<TaskTemplate> getTaskTemplateList() {
		return taskTemplateList;
	}

	public void setTaskTemplateList(List<TaskTemplate> taskTemplateList) {
		this.taskTemplateList = taskTemplateList;
	}

	/**
	 * Add the given {@link TaskTemplate} item to the {@code taskTemplateList}.
	 *
	 * <p>
	 * It sets {@code item.parentTaskTemplate = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTaskTemplateListItem(TaskTemplate item) {
		if (getTaskTemplateList() == null) {
			setTaskTemplateList(new ArrayList<>());
		}
		getTaskTemplateList().add(item);
		item.setParentTaskTemplate(this);
	}

	/**
	 * Remove the given {@link TaskTemplate} item from the {@code taskTemplateList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTaskTemplateListItem(TaskTemplate item) {
		if (getTaskTemplateList() == null) {
			return;
		}
		getTaskTemplateList().remove(item);
	}

	/**
	 * Clear the {@code taskTemplateList} collection.
	 *
	 * <p>
	 * If you have to query {@link TaskTemplate} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearTaskTemplateList() {
		if (getTaskTemplateList() != null) {
			getTaskTemplateList().clear();
		}
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Boolean getIsUniqueTaskForMultipleQuantity() {
		return isUniqueTaskForMultipleQuantity == null ? Boolean.FALSE : isUniqueTaskForMultipleQuantity;
	}

	public void setIsUniqueTaskForMultipleQuantity(Boolean isUniqueTaskForMultipleQuantity) {
		this.isUniqueTaskForMultipleQuantity = isUniqueTaskForMultipleQuantity;
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
		if (!(obj instanceof TaskTemplate)) return false;

		final TaskTemplate other = (TaskTemplate) obj;
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
			.add("delayToStart", getDelayToStart())
			.add("duration", getDuration())
			.add("totalPlannedHrs", getTotalPlannedHrs())
			.add("isUniqueTaskForMultipleQuantity", getIsUniqueTaskForMultipleQuantity())
			.omitNullValues()
			.toString();
	}
}
