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
package com.axelor.team.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Timer;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.project.db.ProjectCategory;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "TEAM_TASK", indexes = { @Index(columnList = "team"), @Index(columnList = "name"), @Index(columnList = "assigned_to"), @Index(columnList = "project"), @Index(columnList = "fullName"), @Index(columnList = "parent_task"), @Index(columnList = "project_category"), @Index(columnList = "team"), @Index(columnList = "product"), @Index(columnList = "unit") })
@Track(fields = { @TrackField(name = "name"), @TrackField(name = "status"), @TrackField(name = "priority"), @TrackField(name = "taskDate"), @TrackField(name = "taskDuration"), @TrackField(name = "taskDeadline"), @TrackField(name = "assignedTo"), @TrackField(name = "sequence"), @TrackField(name = "project"), @TrackField(name = "parentTask"), @TrackField(name = "projectCategory"), @TrackField(name = "progressSelect"), @TrackField(name = "membersUserSet"), @TrackField(name = "description") }, messages = { @TrackMessage(message = "Task created", condition = "true", on = TrackEvent.CREATE), @TrackMessage(message = "Task closed", condition = "status == 'closed'", tag = "success", on = TrackEvent.UPDATE, fields = "status"), @TrackMessage(message = "Urgent", condition = "priority == 'urgent'", tag = "important", on = TrackEvent.UPDATE, fields = "priority"), @TrackMessage(message = "Task updated", condition = "true", on = TrackEvent.UPDATE) }, contents = { @TrackMessage(message = "#{description}", condition = "true", fields = "description") }, files = true)
public class TeamTask extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEAM_TASK_SEQ")
	@SequenceGenerator(name = "TEAM_TASK_SEQ", sequenceName = "TEAM_TASK_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	private String name;

	@Widget(selection = "team.task.status")
	private String status;

	@Widget(selection = "team.task.priority")
	private String priority;

	private Integer sequence = 0;

	private LocalDate taskDate;

	private Integer taskDuration = 0;

	private LocalDate taskDeadline;

	private String relatedModel;

	private String relatedName;

	private Long relatedId = 0L;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedTo;

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Name", search = { "id", "name" })
	@NameColumn
	private String fullName;

	@Widget(title = "Parent task")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TeamTask parentTask;

	@Widget(title = "Category")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectCategory projectCategory;

	@Widget(title = "Progress", selection = "project.task.progress.select")
	private Integer progressSelect = 0;

	@Widget(title = "Team")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TeamTask> teamTaskList;

	@Widget(title = "Followers")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> membersUserSet;

	@Widget(title = "Description")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit unit;

	@Widget(title = "Quantity")
	private BigDecimal quantity = BigDecimal.ZERO;

	@Widget(title = "Unit price")
	private BigDecimal unitPrice = BigDecimal.ZERO;

	@Widget(title = "Currency")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Currency currency;

	@Widget(title = "Planned progress")
	private BigDecimal plannedProgress = BigDecimal.ZERO;

	@Widget(title = "Predecessors tasks")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<TeamTask> finishToStartSet;

	@Widget(title = "Tasks to start before start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<TeamTask> startToStartSet;

	@Widget(title = "Tasks to finish before finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<TeamTask> finishToFinishSet;

	@Widget(title = "Duration hours")
	private BigDecimal durationHours = BigDecimal.ZERO;

	@Widget(title = "Task end")
	private LocalDate taskEndDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Timer> timerList;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public TeamTask() {
	}

	public TeamTask(String name) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public Integer getTaskDuration() {
		return taskDuration == null ? 0 : taskDuration;
	}

	public void setTaskDuration(Integer taskDuration) {
		this.taskDuration = taskDuration;
	}

	public LocalDate getTaskDeadline() {
		return taskDeadline;
	}

	public void setTaskDeadline(LocalDate taskDeadline) {
		this.taskDeadline = taskDeadline;
	}

	public String getRelatedModel() {
		return relatedModel;
	}

	public void setRelatedModel(String relatedModel) {
		this.relatedModel = relatedModel;
	}

	public String getRelatedName() {
		return relatedName;
	}

	public void setRelatedName(String relatedName) {
		this.relatedName = relatedName;
	}

	public Long getRelatedId() {
		return relatedId == null ? 0L : relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public TeamTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(TeamTask parentTask) {
		this.parentTask = parentTask;
	}

	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	public Integer getProgressSelect() {
		return progressSelect == null ? 0 : progressSelect;
	}

	public void setProgressSelect(Integer progressSelect) {
		this.progressSelect = progressSelect;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<TeamTask> getTeamTaskList() {
		return teamTaskList;
	}

	public void setTeamTaskList(List<TeamTask> teamTaskList) {
		this.teamTaskList = teamTaskList;
	}

	/**
	 * Add the given {@link TeamTask} item to the {@code teamTaskList}.
	 *
	 * <p>
	 * It sets {@code item.parentTask = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTeamTaskListItem(TeamTask item) {
		if (getTeamTaskList() == null) {
			setTeamTaskList(new ArrayList<>());
		}
		getTeamTaskList().add(item);
		item.setParentTask(this);
	}

	/**
	 * Remove the given {@link TeamTask} item from the {@code teamTaskList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTeamTaskListItem(TeamTask item) {
		if (getTeamTaskList() == null) {
			return;
		}
		getTeamTaskList().remove(item);
	}

	/**
	 * Clear the {@code teamTaskList} collection.
	 *
	 * <p>
	 * If you have to query {@link TeamTask} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearTeamTaskList() {
		if (getTeamTaskList() != null) {
			getTeamTaskList().clear();
		}
	}

	public Set<User> getMembersUserSet() {
		return membersUserSet;
	}

	public void setMembersUserSet(Set<User> membersUserSet) {
		this.membersUserSet = membersUserSet;
	}

	/**
	 * Add the given {@link User} item to the {@code membersUserSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			setMembersUserSet(new HashSet<>());
		}
		getMembersUserSet().add(item);
	}

	/**
	 * Remove the given {@link User} item from the {@code membersUserSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			return;
		}
		getMembersUserSet().remove(item);
	}

	/**
	 * Clear the {@code membersUserSet} collection.
	 *
	 */
	public void clearMembersUserSet() {
		if (getMembersUserSet() != null) {
			getMembersUserSet().clear();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public BigDecimal getQuantity() {
		return quantity == null ? BigDecimal.ZERO : quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice == null ? BigDecimal.ZERO : unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getPlannedProgress() {
		return plannedProgress == null ? BigDecimal.ZERO : plannedProgress;
	}

	public void setPlannedProgress(BigDecimal plannedProgress) {
		this.plannedProgress = plannedProgress;
	}

	public Set<TeamTask> getFinishToStartSet() {
		return finishToStartSet;
	}

	public void setFinishToStartSet(Set<TeamTask> finishToStartSet) {
		this.finishToStartSet = finishToStartSet;
	}

	/**
	 * Add the given {@link TeamTask} item to the {@code finishToStartSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToStartSetItem(TeamTask item) {
		if (getFinishToStartSet() == null) {
			setFinishToStartSet(new HashSet<>());
		}
		getFinishToStartSet().add(item);
	}

	/**
	 * Remove the given {@link TeamTask} item from the {@code finishToStartSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToStartSetItem(TeamTask item) {
		if (getFinishToStartSet() == null) {
			return;
		}
		getFinishToStartSet().remove(item);
	}

	/**
	 * Clear the {@code finishToStartSet} collection.
	 *
	 */
	public void clearFinishToStartSet() {
		if (getFinishToStartSet() != null) {
			getFinishToStartSet().clear();
		}
	}

	public Set<TeamTask> getStartToStartSet() {
		return startToStartSet;
	}

	public void setStartToStartSet(Set<TeamTask> startToStartSet) {
		this.startToStartSet = startToStartSet;
	}

	/**
	 * Add the given {@link TeamTask} item to the {@code startToStartSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToStartSetItem(TeamTask item) {
		if (getStartToStartSet() == null) {
			setStartToStartSet(new HashSet<>());
		}
		getStartToStartSet().add(item);
	}

	/**
	 * Remove the given {@link TeamTask} item from the {@code startToStartSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToStartSetItem(TeamTask item) {
		if (getStartToStartSet() == null) {
			return;
		}
		getStartToStartSet().remove(item);
	}

	/**
	 * Clear the {@code startToStartSet} collection.
	 *
	 */
	public void clearStartToStartSet() {
		if (getStartToStartSet() != null) {
			getStartToStartSet().clear();
		}
	}

	public Set<TeamTask> getFinishToFinishSet() {
		return finishToFinishSet;
	}

	public void setFinishToFinishSet(Set<TeamTask> finishToFinishSet) {
		this.finishToFinishSet = finishToFinishSet;
	}

	/**
	 * Add the given {@link TeamTask} item to the {@code finishToFinishSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToFinishSetItem(TeamTask item) {
		if (getFinishToFinishSet() == null) {
			setFinishToFinishSet(new HashSet<>());
		}
		getFinishToFinishSet().add(item);
	}

	/**
	 * Remove the given {@link TeamTask} item from the {@code finishToFinishSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToFinishSetItem(TeamTask item) {
		if (getFinishToFinishSet() == null) {
			return;
		}
		getFinishToFinishSet().remove(item);
	}

	/**
	 * Clear the {@code finishToFinishSet} collection.
	 *
	 */
	public void clearFinishToFinishSet() {
		if (getFinishToFinishSet() != null) {
			getFinishToFinishSet().clear();
		}
	}

	public BigDecimal getDurationHours() {
		return durationHours == null ? BigDecimal.ZERO : durationHours;
	}

	public void setDurationHours(BigDecimal durationHours) {
		this.durationHours = durationHours;
	}

	public LocalDate getTaskEndDate() {
		return taskEndDate;
	}

	public void setTaskEndDate(LocalDate taskEndDate) {
		this.taskEndDate = taskEndDate;
	}

	public List<Timer> getTimerList() {
		return timerList;
	}

	public void setTimerList(List<Timer> timerList) {
		this.timerList = timerList;
	}

	/**
	 * Add the given {@link Timer} item to the {@code timerList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTimerListItem(Timer item) {
		if (getTimerList() == null) {
			setTimerList(new ArrayList<>());
		}
		getTimerList().add(item);
	}

	/**
	 * Remove the given {@link Timer} item from the {@code timerList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTimerListItem(Timer item) {
		if (getTimerList() == null) {
			return;
		}
		getTimerList().remove(item);
	}

	/**
	 * Clear the {@code timerList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearTimerList() {
		if (getTimerList() != null) {
			getTimerList().clear();
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
		if (!(obj instanceof TeamTask)) return false;

		final TeamTask other = (TeamTask) obj;
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
			.add("status", getStatus())
			.add("priority", getPriority())
			.add("sequence", getSequence())
			.add("taskDate", getTaskDate())
			.add("taskDuration", getTaskDuration())
			.add("taskDeadline", getTaskDeadline())
			.add("relatedModel", getRelatedModel())
			.add("relatedName", getRelatedName())
			.add("relatedId", getRelatedId())
			.add("fullName", getFullName())
			.omitNullValues()
			.toString();
	}
}
