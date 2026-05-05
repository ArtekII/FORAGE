package dao;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import map.Mapping;

public class GenericDAO implements InterfaceDAO {
    private UtilDAO utils;
    private static Map<String, Mapping> map;

    public UtilDAO getUtils() {
        return utils;
    }

    public void setUtils(UtilDAO utils) {
        this.utils = utils;
    }

    public static void setMap(Map<String, Mapping> m) {
        map = m;
    }

    private Mapping getMapping(Class<?> clazz) {
        if (map == null) return null;
        return map.get(clazz.getName());
    }

    public static String capitaliser(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Method getMethodGetter(Class<?> clazz, String col) throws Exception {
        return clazz.getDeclaredMethod("get"+capitaliser(col));
    }

    public Method getMethodSetter(Class<?> clazz, String col, Class<?> type) throws Exception {
        return clazz.getDeclaredMethod("set"+ capitaliser(col),type);
    }

    @Override
    public void save(Object o) throws Exception {
        try (Connection con = utils.getConnection()) {
            save(con, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Connection con, Object o) throws Exception {
        Class<?> clazz = o.getClass();
        Mapping mapping = getMapping(clazz);

        String tableName = mapping.getTableName();
        String[] colsTab = mapping.getColonnes();

        StringBuffer cols = new StringBuffer();
        StringBuffer temp = new StringBuffer();

        for (String col : colsTab) {
            if ("id".equals(col)) continue;
            cols.append(col).append(",");
            temp.append("?").append(",");
        }

        if (cols.length() > 0) {
            cols.setLength(cols.length() - 1);
            temp.setLength(temp.length() - 1);
        }

        String sql = "INSERT INTO " + tableName + "(" + cols + ") VALUES(" + temp + ");";
        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            for (String c : colsTab) {
                if ("id".equals(c)) continue;
                Method method = getMethodGetter(clazz, c);
                pst.setObject(index++, method.invoke(o));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    Method method = getMethodSetter(clazz, "id", int.class);
                    method.invoke(o, rs.getInt(1));
                }
            }
        }
    }



    @Override
    public void delete(Object o) throws Exception {
        try (Connection con = utils.getConnection()) {
            delete(con, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Connection con, Object o) throws Exception {
        Class<?> clazz = o.getClass();
        String tableName = clazz.getSimpleName();

        Method methodId = getMethodGetter(clazz, "id");
        int id = (int) methodId.invoke(o);

        String sql = "DELETE FROM " + tableName + " WHERE id = ?;";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }



    @Override
    public void update(Object o) throws Exception {
        try (Connection con = utils.getConnection()) {
            update(con, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Connection con, Object o) throws Exception {
        Class<?> clazz = o.getClass();
        Mapping mapping = getMapping(clazz);

        String tableName = mapping.getTableName();
        String[] colsTab = mapping.getColonnes();

        StringBuffer cols = new StringBuffer();
        int paramCount = 0;

        for (String c : colsTab) {
            if ("id".equals(c)) {
                continue;
            }
            cols.append(c).append("=?,");
            paramCount++;
        }

        if (paramCount > 0) {
            cols.setLength(cols.length() - 1);
        }

        String sql = "UPDATE " + tableName + " SET " + cols + " WHERE id=?;";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            int index = 1;
            for (String c : colsTab) {
                if ("id".equals(c)) {
                    continue;
                }
                Method method = getMethodGetter(clazz, c);
                pst.setObject(index++, method.invoke(o));
            }
            Method getId = getMethodGetter(clazz, "id");
            pst.setObject(index, getId.invoke(o));
            pst.executeUpdate();
        }
    }


    @Override
    public <T> T findById(Object id, Class<T> clazz) throws Exception {
        try (Connection con = utils.getConnection()) {
            return findById(con, id, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T findById(Connection con, Object id, Class<T> clazz) throws Exception {
        Mapping mapping = getMapping(clazz);

        String tableName = (mapping != null) ? mapping.getTableName() : clazz.getSimpleName();
        Field[] fields = (mapping != null) ? mapping.getFields() : clazz.getDeclaredFields();

        String sql = "SELECT * FROM "+ tableName + " WHERE id = ?";
        try(PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setObject(1, id);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                T o = clazz.getDeclaredConstructor().newInstance();
                for (Field f : fields) {
                    Method method = getMethodSetter(clazz, f.getName(), f.getType());
                    method.invoke(o, rs.getObject(f.getName()));
                }
                return o;
            }            
        }
        return null;
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        try(Connection con = utils.getConnection()) {
            lObjects = findAll(con, clazz);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lObjects;
    }

    public <T> List<T> findAll(Connection con, Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        Mapping mapping = getMapping(clazz);

        String tableName = (mapping != null) ? mapping.getTableName() : clazz.getSimpleName();
        Field[] fields = (mapping != null) ? mapping.getFields() : clazz.getDeclaredFields();
        String sql = "SELECT * FROM "+tableName;

        try(PreparedStatement pst = con.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()) {
                T o = clazz.getDeclaredConstructor().newInstance();
                for (Field f : fields) {
                    Method methodSet = clazz.getDeclaredMethod("set"+capitaliser(f.getName()), f.getType());
                    methodSet.invoke(o, rs.getObject(f.getName()));
                }
                lObjects.add(o);
            }
        }
        
        return lObjects;
    }
    
    @Override
    public <T> List<T> findByCriteria(Object criteria, Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        try(Connection con = utils.getConnection()) {
            lObjects = findByCriteria(con, criteria, clazz);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lObjects;
    }

    public <T> List<T> findByCriteria(Connection con, Object criteria, Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        Mapping mapping = getMapping(clazz);

        String tableName = (mapping != null) ? mapping.getTableName() : clazz.getSimpleName();
        Field[] fields = (mapping != null) ? mapping.getFields() : clazz.getDeclaredFields();

        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE 1=1");
        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(criteria);
            if (value != null) {
                sql.append(" AND ").append(f.getName()).append(" = ?");
            }
        }

        try(PreparedStatement pst = con.prepareStatement(sql.toString())) {
            int index = 1;
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(criteria);
                if (value != null) {
                    pst.setObject(index++, value);
                }
            }
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()) {
                T o = clazz.getDeclaredConstructor().newInstance();
                for (Field f : clazz.getDeclaredFields()) {
                    Method methodSet = clazz.getDeclaredMethod("set"+capitaliser(f.getName()), f.getType());
                    methodSet.invoke(o, rs.getObject(f.getName()));
                }
                lObjects.add(o);
            }
        }
        
        return lObjects;
    }

    //Utilisant un parametre de type
    public <T> List<T> findAllGeneric(Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        try(Connection con = utils.getConnection()) {
            lObjects = findAllGeneric(con, clazz);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lObjects;
    }

    public <T> List<T> findAllGeneric(Connection con, Class<T> clazz) throws Exception {
        List<T> lObjects = new ArrayList<>();
        String tableName = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();
        String sql = "SELECT * FROM "+tableName;

        try(PreparedStatement pst = con.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()) {
                T o = clazz.getDeclaredConstructor().newInstance();
                for (Field f : fields) {
                    Method methodSet = clazz.getDeclaredMethod("set"+capitaliser(f.getName()), f.getType());
                    methodSet.invoke(o, rs.getObject(f.getName()));
                }
                lObjects.add(o);
            }
        }

        return lObjects;
    }
}
