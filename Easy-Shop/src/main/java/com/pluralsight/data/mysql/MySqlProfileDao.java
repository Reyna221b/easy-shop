package com.pluralsight.data.mysql;

import org.springframework.stereotype.Component;
import com.pluralsight.data.ProfileDao;
import com.pluralsight.models.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getUserById(int id) {


        Profile profile = null;
        try(Connection connection = getConnection();){
            String sql = "SELECT * FROM profiles WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                profile = new Profile(id,firstName,lastName,phone,email,address,city,state,zip);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return profile;
    }

    @Override
    public void update( Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip = ? WHERE user_id = ?";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, profile.getFirstName());
            preparedStatement.setString(2, profile.getLastName());
            preparedStatement.setString(3, profile.getPhone());
            preparedStatement.setString(4, profile.getEmail());
            preparedStatement.setString(5, profile.getAddress());
            preparedStatement.setString(6, profile.getCity());
            preparedStatement.setString(7, profile.getState());
            preparedStatement.setString(8, profile.getZip());
            preparedStatement.setInt(9, profile.getUserId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
