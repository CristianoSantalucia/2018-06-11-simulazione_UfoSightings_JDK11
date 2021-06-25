package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ufo.model.Adiacenza;
import it.polito.tdp.ufo.model.AnnoAvvistamento;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.State;

public class SightingsDAO
{

	public List<Sighting> getSightings()
	{
		String sql = "SELECT * FROM sighting";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<AnnoAvvistamento> getAnniAvvistamenti()
	{
		String sql = "SELECT YEAR(s.datetime) anno, COUNT(*) avvistamenti "
				+ "FROM sighting s "
				+ "WHERE s.country = 'us' "
				+ "GROUP BY anno "
				+ "HAVING avvistamenti > 0 "; 
		
		List<AnnoAvvistamento> result = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				result.add(new AnnoAvvistamento(res.getInt("anno"), res.getInt("avvistamenti")));
			}

			conn.close();
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(Map<String, State> vertici, int year)
	{
		String sql = "SELECT s.state state, COUNT(*) num "
				+ "FROM sighting s "
				+ "WHERE s.country = 'us' "
				+ "		AND YEAR (s.datetime) = ? "
				+ "GROUP BY s.state "
				+ "HAVING num > 0 "; 
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				State s = new State(res.getString("state")); 
				vertici.putIfAbsent(s.getName(), s);
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<Sighting> getAvvistamenti(int year)
	{
		String sql = "SELECT * "
				+ "FROM sighting s "
				+ "WHERE s.country = 'us' "
				+ "		AND YEAR (s.datetime) = ? "; 

		List<Sighting> list = new ArrayList<>();
		
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza>  getAdiacenze(Map<String, State> vertici, int year)
	{
		String sql = "SELECT s1.state state1, s2.state state2 "
					+ "FROM sighting s1, sighting s2 "
					+ "WHERE s1.state < s2.state "
					+ "		AND s1.country = 'us' "
					+ "		AND s2.country = 'us' "
					+ "		AND YEAR (s1.datetime) = ? "
					+ "		AND YEAR (s2.datetime) = YEAR (s1.datetime) "
					+ "		AND s2.datetime > s1.datetime "
					+ "GROUP BY s1.state, s2.state "; 

		List<Adiacenza> list = new ArrayList<>();
		
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				State s1 = vertici.get(res.getString("state1")); 
				State s2 = vertici.get(res.getString("state2")); 
				if(s1 != null && s2 != null)
				{
					list.add(new Adiacenza(s1,s2));
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
}
