package com.app.DashBoard.Event;

import com.app.Auth.Person;
import com.app.DashBoard.View.DashBoardViewType;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashBoardEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

//    public static final class TransactionReportEvent {
//        private final Collection<Transaction> transactions;
//
//        public TransactionReportEvent(final Collection<Transaction> transactions) {
//            this.transactions = transactions;
//        }
//
//        public Collection<Transaction> getTransactions() {
//            return transactions;
//        }
//    }
//
    public static final class PostViewChangeEvent {
        private final DashBoardViewType view;

        public PostViewChangeEvent(final DashBoardViewType view) {
            this.view = view;
        }

        public DashBoardViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static final class UserNewEvent {
    	private final Person person;
    	public UserNewEvent(final Person person) {
    		this.person = person;
    	}
    	
    	public Person getPerson() {
    		return this.person;
    	}
    	
    }
    
    public static class UpdateUserEvent {
    }


    public static class ProfileUpdatedEvent {
    }
    
    public static class DogUpdatedEvent {
    }
}
