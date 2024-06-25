package com.pluralsight.data.mysql;

import com.pluralsight.data.CategoryDao;
import com.pluralsight.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{

    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();

        try(Connection connection = getConnection())
        {
            String sql = """
                    SELECT *
                    FROM categories;
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery();

            while(row.next())
            {
                Category category = mapRow(row);

                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error w/ categories", e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        try(Connection connection = getConnection())
        {
            String sql = """
                    SELECT *
                    FROM categories
                    WHERE category_Id = ?;
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            ResultSet row = statement.executeQuery();

            if(row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
        }

        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        int newId = 0;

        try(Connection connection = getConnection())
        {
            String sql = """
                    INSERT INTO categories (name, description)
                    VALUES (?, ?);
                    """;
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            newId = generatedKeys.getInt(1);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error w/ creating category", e);
        }

        return getById(newId);
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try(Connection connection = getConnection())
        {
            String sql = """
                    UPDATE categories
                    SET name = ?
                        , Description = ?
                    WHERE category_Id = ?;
                    """;
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error updating category", e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try(Connection connection = getConnection())
        {
            String sqlProducts = """
                    UPDATE products
                    SET category_Id = NULL
                    WHERE category_Id = ?;
                    """;
            PreparedStatement statementProducts = connection.prepareStatement(sqlProducts);
            statementProducts.setInt(1, categoryId);
            statementProducts.executeUpdate();


            String sql = """
                    DELETE FROM categories
                    WHERE category_Id = ?;
                    """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
