# reports

## Exemplo call JDBCTemplate procedure in, out.
    ...
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
    .withSchemaName(schema)
    .withCatalogName(package)
    .withProcedureName(procedure)();
    ...
    jdbcCall.addDeclaredParameter(new SqlParameter(paramName, OracleTypes.NUMBER));
    ...
    jdbcCall.execute(callParams);
    ...

    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
    .withProcedureName("STORED_PROCEDURE_NAME");
    
    Map<String, Object> inParamMap = new HashMap<String, Object>();
    inParamMap.put("firstName", "FirstNameValue");
    inParamMap.put("lastName", "LastNameValue");
    SqlParameterSource in = new MapSqlParameterSource(inParamMap);
    
    Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
    System.out.println(simpleJdbcCallResult);



## Para simples use apenas

    jdbcTemplate.update("call SOME_PROC (?, ?)", param1, param2);


## Usando CallableStatement

    connection = jdbcTemplate.getDataSource().getConnection();
    CallableStatement callableStatement = connection.prepareCall("{call STORED_PROCEDURE_NAME(?, ?, ?)}");
    callableStatement.setString(1, "FirstName");
    callableStatement.setString(2, " LastName");
    callableStatement.registerOutParameter(3, Types.VARCHAR);
    callableStatement.executeUpdate();

## Usando CallableStatementCreator
    List paramList = new ArrayList();
    paramList.add(new SqlParameter(Types.VARCHAR));
    paramList.add(new SqlParameter(Types.VARCHAR));
    paramList.add(new SqlOutParameter("msg", Types.VARCHAR));

    Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {

    @Override
    public CallableStatement createCallableStatement(Connection connection)
    throws SQLException {

    CallableStatement callableStatement = connection.prepareCall("{call STORED_PROCEDURE_NAME(?, ?, ?)}");
    callableStatement.setString(1, "FirstName");
            callableStatement.setString(2, " LastName");
            callableStatement.registerOutParameter(3, Types.VARCHAR);
    return callableStatement;

    }
    }, paramList);


### Para mais 

 https://stackoverflow.com/questions/9361538/spring-jdbc-template-for-calling-stored-procedures

