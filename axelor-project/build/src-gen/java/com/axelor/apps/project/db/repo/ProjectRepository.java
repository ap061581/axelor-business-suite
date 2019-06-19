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
package com.axelor.apps.project.db.repo;

import com.axelor.apps.project.db.Project;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ProjectRepository extends JpaRepository<Project> {

	public ProjectRepository() {
		super(Project.class);
	}

	public Project findByCode(String code) {
		return Query.of(Project.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public Query<Project> findAllByParentProject(Project parentProject) {
		return Query.of(Project.class)
				.filter("self.parentProject = :parentProject")
				.bind("parentProject", parentProject);
	}

	public Project findByName(String name) {
		return Query.of(Project.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	public static final Integer TYPE_PROJECT = 1;
				public static final Integer TYPE_PHASE = 2;

	public static final Integer STATE_PRE_SALES = 2;
				public static final Integer STATE_NEW = 1;
				public static final Integer STATE_IN_PROGRESS = 3;
				public static final Integer STATE_ON_WARRANTY = 4;
				public static final Integer STATE_FINISHED = 5;
				public static final Integer STATE_ONHOLD = 6;
				public static final Integer STATE_CANCELED = 7;
}

