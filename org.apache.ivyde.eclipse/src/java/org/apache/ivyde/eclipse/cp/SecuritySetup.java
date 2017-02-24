/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.ivyde.eclipse.cp;

public class SecuritySetup {

    private String host="";
    
    private String realm="";

    private String userName="";

    private String pwd="";

    /**
     * Default constructor
     */
    public SecuritySetup() {
        // default constructor
    }

    public void set(SecuritySetup setup) {
        this.host = setup.host;
        this.realm = setup.realm;
        this.userName = setup.userName;
        this.pwd = setup.pwd;
    }

    
    
    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * @param realm
     *            the realm to set
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd
     *            the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString(){
        return "[host='"+this.host+", realm='"+this.realm+"' user='"+this.userName+"' pwd='"+this.pwd+"']";
    }
}
