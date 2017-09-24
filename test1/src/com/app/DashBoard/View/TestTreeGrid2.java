package com.app.DashBoard.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBShowPrintKatalog;
import com.vaadin.data.HierarchyData;
import com.vaadin.data.provider.InMemoryHierarchicalDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TestTreeGrid2 extends VerticalLayout implements View {
	Random random;

	public TestTreeGrid2() {
		random = new Random();
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());
		//addComponent(buildWorkingArea());
		DBShowPrintKatalog dbs = new DBShowPrintKatalog();

		dbs.uebertrageHunde();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("MeineHunde");
		title.setSizeUndefined();

		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		header.addComponent(title);

		return header;
	}

	private Component buildWorkingArea() {
		VerticalLayout mainLayout = new VerticalLayout();
		//ShowRing ring = new ShowRing();
		// alle datena ufbauen....
        Project rootProject = new Project("All Projects");
        rootProject.addSubProjects(generateProjectsForYears(2010, 2016));
 
        HierarchyData<Project> data = new HierarchyData<>();
        data.addItem(null, rootProject);
        rootProject.flattened().forEach(
                project -> data.addItems(project, project.getSubProjects()));
 
        TreeGrid<Project> treeGrid = new TreeGrid<>();
        treeGrid.setDataProvider(new InMemoryHierarchicalDataProvider<Project>(data));
        
        treeGrid.addColumn(Project::getName).setCaption("Project Name").setId("name-column");
        treeGrid.addColumn(Project::getHoursDone).setCaption("Hours Done");
        treeGrid.addColumn(Project::getLastModified).setCaption("Last Modified");
        
        treeGrid.addCollapseListener(event -> {
            Notification.show(
                    "Project '" + event.getCollapsedItem().getName() + "' collapsed.",
                    Type.TRAY_NOTIFICATION);
        });
        treeGrid.addExpandListener(event -> {
            Notification.show(
                    "Project '" + event.getExpandedItem().getName()+ "' expanded.",
                    Type.TRAY_NOTIFICATION);
        });
		mainLayout.addComponent(treeGrid);
		return (mainLayout);

	}
	
	class Project {
		 
        private final List<Project> subProjects;
        private String name;
 
        public Project(String name) {
            this.name = name;
            subProjects = new ArrayList<>();
        }
 
        public String getName() {
            return name;
        }
 
        public Stream<Project> getSubProjects() {
            return subProjects.stream();
        }
 
        public void addSubProjects(List<Project> subProjects) {
            this.subProjects.addAll(subProjects);
        }
 
        public void addSubProject(Project subProject) {
            subProjects.add(subProject);
        }
 
        public int getHoursDone() {
            return getSubProjects().map(project -> project.getHoursDone())
                    .reduce(0, Integer::sum);
        }
 
        public Date getLastModified() {
            return getSubProjects().map(project -> project.getLastModified())
                    .max(Date::compareTo).orElse(null);
        }
 
        public Stream<Project> flattened() {
            return Stream.concat(Stream.of(this),
                    getSubProjects().flatMap(Project::flattened));
        }
    }
 
    class LeafProject extends Project {
 
        private int hoursDone;
        private Date lastModified;
 
        public LeafProject(String name, int hoursDone, int year) {
            super(name);
            this.hoursDone = hoursDone;
            lastModified = new Date(year - 1900, random.nextInt(12),
                    random.nextInt(10));
        }
 
        @Override
        public int getHoursDone() {
            return hoursDone;
        }
 
        public void setHoursDone(int hoursDone) {
            this.hoursDone = hoursDone;
            lastModified = new Date();
        }
 
        @Override
        public Date getLastModified() {
            return lastModified;
        }
    }
 
 

 
    private List<Project> generateProjectsForYears(int startYear, int endYear) {
        List<Project> projects = new ArrayList<>();
 
        for (int year = startYear; year <= endYear; year++) {
            Project yearProject = new Project("Year " + year);
 
            for (int i = 1; i < 2 + random.nextInt(5); i++) {
                Project customerProject = new Project("Customer Project " + i);
                customerProject.addSubProjects(Arrays.asList(
                        new LeafProject("Implementation", random.nextInt(100), year),
                        new LeafProject("Planning", random.nextInt(10), year),
                        new LeafProject("Prototyping", random.nextInt(20), year)));
                yearProject.addSubProject(customerProject);
            }
            projects.add(yearProject);
        }
        return projects;
    }
}
