package com.app.dbIO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.SQLUtil;
import com.vaadin.data.util.sqlcontainer.TemporaryRowId;
import com.vaadin.data.util.sqlcontainer.query.FreeformStatementDelegate;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;

public class OercMitgliedDelegate implements FreeformStatementDelegate {

	private List<Filter> filters;
	private List<OrderBy> orderBys;

	@Deprecated
	public String getQueryString(int offset, int limit)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Use getQueryStatement method.");
	}

	public StatementHelper getQueryStatement(int offset, int limit)
			throws UnsupportedOperationException {
		StatementHelper sh = new StatementHelper();
		StringBuffer query = new StringBuffer("SELECT * FROM tabMitglieder ");
		if (filters != null) {
			query.append(QueryBuilder.getWhereStringForFilters(filters, sh));
		}
		query.append(getOrderByString());
		if (offset != 0 ) { 
			query.append(" OFFSET ").append(offset).append(" ROWS ");
		}
		
		if (limit != 0) {
			query.append(" FETCH ").append(limit).append(" ROWS ONLY ");
		}
		System.out.println("query: " + query.toString());
		sh.setQueryString(query.toString());
		return sh;
	}

	private String getOrderByString() {
		StringBuffer orderBuffer = new StringBuffer("");
		if (orderBys != null && !orderBys.isEmpty()) {
			orderBuffer.append(" ORDER BY ");
			OrderBy lastOrderBy = orderBys.get(orderBys.size() - 1);
			for (OrderBy orderBy : orderBys) {
				orderBuffer.append(SQLUtil.escapeSQL(orderBy.getColumn()));
				if (orderBy.isAscending()) {
					orderBuffer.append(" ASC");
				} else {
					orderBuffer.append(" DESC");
				}
				if (orderBy != lastOrderBy) {
					orderBuffer.append(", ");
				}
			}
		}
		return orderBuffer.toString();
	}

	@Deprecated
	public String getCountQuery() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Use getCountStatement method.");
	}

	public StatementHelper getCountStatement()
			throws UnsupportedOperationException {
		StatementHelper sh = new StatementHelper();
		StringBuffer query = new StringBuffer(
				"SELECT COUNT(*) FROM tabMitglieder ");
		if (filters != null) {
			query.append(QueryBuilder.getWhereStringForFilters(filters, sh));
		}
		sh.setQueryString(query.toString());
		return sh;
	}

	public void setFilters(List<Filter> filters)
			throws UnsupportedOperationException {
		this.filters = filters;
	}

	public void setOrderBy(List<OrderBy> orderBys)
			throws UnsupportedOperationException {
		this.orderBys = orderBys;
	}

	public int storeRow(Connection conn, RowItem row) throws SQLException {
		PreparedStatement statement = null;
		if (row.getId() instanceof TemporaryRowId) {

		} else {
			statement = conn
					.prepareStatement("UPDATE SCHAUHUND SET BOB = ?, bestehrenring = ?, platzehrenring = ?, BOD = ?, BIS = ? "
							+ "WHERE idschauhund = ?");
			setRowValues(statement, row);
			statement.setInt(6, (Integer) row.getItemProperty("idschauhund")
					.getValue());
		}

		int retval = statement.executeUpdate();
		statement.close();
		return retval;
	}

	private void setRowValues(PreparedStatement statement, RowItem row)
			throws SQLException {
		statement.setString(1, (String) row.getItemProperty("BOB").getValue());
		statement.setString(2, (String) row.getItemProperty("bestehrenring")
				.getValue());
		statement.setString(3, (String) row.getItemProperty("platzehrenring")
				.getValue());
		statement.setString(4, (String) row.getItemProperty("BOD").getValue());
		statement.setString(5, (String) row.getItemProperty("BIS").getValue());
	}

	public boolean removeRow(Connection conn, RowItem row)
			throws UnsupportedOperationException, SQLException {
		PreparedStatement statement = conn
				.prepareStatement("DELETE FROM schau WHERE idschauhund = ?");
		statement.setInt(1, (Integer) row.getItemProperty("idschauhund")
				.getValue());
		int rowsChanged = statement.executeUpdate();
		statement.close();
		return rowsChanged == 1;
	}

	@Deprecated
	public String getContainsRowQueryString(Object... keys)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"Please use getContainsRowQueryStatement method.");
	}

	public StatementHelper getContainsRowQueryStatement(Object... keys)
			throws UnsupportedOperationException {
		StatementHelper sh = new StatementHelper();
		StringBuffer query = new StringBuffer(
				"SELECT * FROM TabMitglieder WHERE IDMitglieder = ?");
		sh.addParameterValue(keys[0]);
		sh.setQueryString(query.toString());
		return sh;
	}

}
