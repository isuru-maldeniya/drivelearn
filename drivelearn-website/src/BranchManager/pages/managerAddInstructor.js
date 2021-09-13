import React, {useState} from 'react';
import '../../App.css';
import './../managerViewStudent.css';
import Navbar from '../Navbar';
import Sidebar from '../managerSidebar';
import axios from 'axios';


const ManagerAddInstructors = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  
  const openSidebar = () => {
     setSidebarOpen(true);
  };

  const closeSidebar = () => {
    setSidebarOpen(false);
 };

 const[state, setState] = useState({
  moNumber:'',
  fullName: '',
  username: '',
  password: '',
  branch: '',
  nid: '',
  role: 2
});

const handleChange = (e) => {
setState({
    ...state,
    [e.target.name]: e.target.value
}) 

}

const handleSubmit = (e) =>{
e.preventDefault()
axios.post('http://localhost:8080/drivelearn/addInstructors',state)

console.log(state);

}

 
return (
  <div className="container">
      <Navbar sidebarOpen={sidebarOpen} openSidebar={openSidebar} />
    <main>
       <div className="main__container">
          <div className="main__title">
              <div className="main__greeting">
              <h1>Add Instructor</h1>
              <p>DriveLEARN</p>
              </div>
          </div>
        <center>
        <div className="charts__rightt">
        <div className="charts__right__title">
          <div>
            <p>Basic Details</p>
          </div>
        </div>

        <form className="charts__rightt__cardss" onSubmit={handleSubmit}>
          <div className="card-p">
              <p className="text">Full Name</p>
              <input className="data" type="text" name="fullName" id="firstname" placeholder="Full Name" value={state.fullName} onChange={handleChange} required />
              {/* <div class="alert-danger" id="firstNameError">
                 * First name can't be empty and must contain only letters
              </div> */}
          </div>
          
         
         
          <div className="card-p">
              <p className="text">NIC</p>
              <input className="data" type="text" name="nid" id="firstname" placeholder="NIC" value={state.nid} onChange={handleChange} required />
              {/* <div class="alert-danger" id="firstNameError">
                 * First name can't be empty and must contain only letters
              </div> */}
          </div>
        
         
       
          <div className="card-p">
              <p className="text">Mobile</p>
              <input className="data" type="text" name="moNumber" id="firstname" placeholder="Mobile" value={state.moNumber} onChange={handleChange} required  />
              {/* <div class="alert-danger" id="firstNameError">
                 * First name can't be empty and must contain only letters
              </div> */}
          </div>


          <div className="card-p">
              <p className="text">Username</p>
              <input className="data" type="text" id="firstname" placeholder="Username" name="username" value={state.username} onChange={handleChange}  required />
              {/* <div class="alert-danger" id="firstNameError">
                 * First name can't be empty and must contain only letters
              </div> */}
          </div>
          <div className="card-p">
              <p className="text">Password</p>
              <input className="data" type="password"  name="password" id="firstname" placeholder="******" value={state.password} onChange={handleChange} required />
              {/* <div class="alert-danger" id="firstNameError">
                 * First name can't be empty and must contain only letters
              </div> */}
          </div>
         

          <div className="card-p">
              <p className="text">Branch</p>
              <select className="option1" name="branch" onChange={handleChange} required>
              <option disabled="disabled" selected="selected">--Choose Option--</option>
              <option value ="mathugama">mathugama</option>
              <option value ="kaluthatara">kaluthatara</option>
              <option value ="Aluthgama">Aluthgama</option>
             </select>
          </div>
          <center>
          <input type="submit" value="Update" className="update-btn" />
          &nbsp;&nbsp;&nbsp;
          <input type="Reset" value="Clear" className="reset1-btn" />
          </center>
        </form>
      </div>
      </center>
      </div>
    </main>
      <Sidebar sidebarOpen={sidebarOpen} closeSidebar={closeSidebar} />
  </div>
);
  
  }
  
  export default ManagerAddInstructors;