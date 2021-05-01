package dao;

public class FoodJDBCDAO {
  /*  private DataSource dataSource;

    public FoodJDBCDAO(DataSource theDataSource) {
        dataSource = theDataSource;
    }



    @Override
    public List<FoodItem> getFoodItems() throws Exception {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from student order by last_name";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                Student tempStudent = new Student(id, firstName, lastName, email);
                foodItems.add(tempStudent);
            }
            return foodItems;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();   // doesn't really close it ... just puts back in connection pool
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    @Override
    public void addFoodItem(FoodItem theFoodItem) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into student "
                    + "(first_name, last_name, email) "
                    + "values (?, ?, ?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    @Override
    public FoodItem getFoodItem(String theFoodItemId) throws Exception {
        Student theStudent = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;
        try {
            studentId = Integer.parseInt(theStudentId);
            myConn = dataSource.getConnection();
            String sql = "select * from student where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, studentId);
            myRs = myStmt.executeQuery();
            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                theStudent = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student id: " + studentId);
            }
            return theStudent;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    @Override
    public void updateFoodItem(FoodItem theFoodItem) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "update student "
                    + "set first_name=?, last_name=?, email=? "
                    + "where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    @Override
    public void deleteFoodItem(String theFoodItemId) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            int studentId = Integer.parseInt(theStudentId);
            myConn = dataSource.getConnection();
            String sql = "delete from student where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, studentId);
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }*/
}














