package com.app.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.tools.ant.taskdefs.Property;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbIO.DBConnection;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

/**
 * @author Ondrej Kvasnovsky
 */
public class UserService implements UserDetailsService,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		TableQuery q1 = new TableQuery("user",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");
		try {

			SQLContainer personContainer = new SQLContainer(q1);
			personContainer.addContainerFilter(new Equal("email",username));
			System.out.println("username:" + username);
			System.out.println("in userseervice"  + personContainer.size());
			
			if (personContainer.size() == 0) {
				throw new UsernameNotFoundException("user unbekannt");
			}
			
			authorities.add(new SimpleGrantedAuthority("CLIENT"));
			User user = new User(username, personContainer.getItem(personContainer.firstItemId()).getItemProperty("password").getValue().toString(), true, true, false, false,
					authorities);
			return user;
		} catch (SQLException e) {
			return null;
		}

		// fetch user from e.g. DB
		//System.out.println("bin in userservice");
		/*
		 * if ("client".equals(username)) { authorities.add(new
		 * SimpleGrantedAuthority("CLIENT")); User user = new User(username,
		 * "pass", true, true, false, false, authorities); return user; } if
		 * ("admin".equals(username)) { authorities.add(new
		 * SimpleGrantedAuthority("ADMIN")); User user = new User(username,
		 * "pass", true, true, false, false, authorities); return user; } else {
		 * return null; }
		 */

	}
}
