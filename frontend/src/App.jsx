
import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [leads, setLeads] = useState([]);

  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    company: ""
  });

  const [editingLead, setEditingLead] = useState(null);

  const [editForm, setEditForm] = useState({
    name: "",
    email: "",
    phone: "",
    company: ""
  });

  const fetchLeads = () => {
    fetch("http://localhost:8080/leads")
      .then(response => response.json())
      .then(data => setLeads(data))
      .catch(error => console.error("Error:", error));
  };

  useEffect(() => {
    fetchLeads();
  }, []);

  const handleChange = (event) => {
    setForm({
      ...form,
      [event.target.name]: event.target.value
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    fetch("http://localhost:8080/addLead", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: new URLSearchParams(form)
    })
      .then(response => {
        if (!response.ok) throw new Error("Could not add lead");

        setForm({
          name: "",
          email: "",
          phone: "",
          company: ""
        });

        fetchLeads();
      })
      .catch(error => console.error("Error:", error));
  };

  const startEdit = (lead) => {
    setEditingLead(lead);

    setEditForm({
      name: lead.name,
      email: lead.email,
      phone: lead.phone,
      company: lead.company
    });
  };

  const handleEditChange = (event) => {
    setEditForm({
      ...editForm,
      [event.target.name]: event.target.value
    });
  };

  const handleUpdate = (event) => {
    event.preventDefault();

    const updatedLead = {
      id: editingLead.id,
      ...editForm
    };

    fetch("http://localhost:8080/updateLead", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: new URLSearchParams(updatedLead)
    })
      .then(response => {
        if (!response.ok) throw new Error("Could not update lead");

        setEditingLead(null);
        fetchLeads();
      })
      .catch(error => console.error("Error:", error));
  };

  const handleDelete = (id) => {
    fetch(`http://localhost:8080/deleteLead?id=${id}`, {
      method: "DELETE"
    })
      .then(response => {
        if (!response.ok) throw new Error("Could not delete lead");

        fetchLeads();
      })
      .catch(error => console.error("Error:", error));
  };

  return (
    <div className="dashboard">

      <aside className="sidebar">
        <h2>LeadFlow.</h2>

        <p className="menu-label">MENU</p>
        <div className="menu-item active">▦ Dashboard</div>
        <div className="menu-item">♙ All Leads</div>
        <div className="menu-item">⚙ Settings</div>

        <div className="sidebar-bottom">
          Lead Generator CRM
          <p>Version 1.0</p>
        </div>
      </aside>

      <main className="main-content">

        <header className="topbar">
          <div>
            <h1>Dashboard</h1>
            <p>Welcome back! Here's your lead overview.</p>
          </div>

          <span className="profile">Admin</span>
        </header>

        <section className="stats">

          <div className="stat-card">
            <p>Total Leads</p>
            <h2>{leads.length}</h2>
            <span>All registered leads</span>
          </div>

          <div className="stat-card">
            <p>Companies</p>
            <h2>{new Set(leads.map(lead => lead.company)).size}</h2>
            <span>Unique companies</span>
          </div>

          <div className="stat-card">
            <p>Contacts</p>
            <h2>{leads.filter(lead => lead.email).length}</h2>
            <span>Leads with email</span>
          </div>

        </section>

        <section className="add-lead-section">

          <h2>Add New Lead</h2>

          <form onSubmit={handleSubmit} className="lead-form">

            <input
              type="text"
              name="name"
              placeholder="Name"
              value={form.name}
              onChange={handleChange}
              required
            />

            <input
              type="email"
              name="email"
              placeholder="Email"
              value={form.email}
              onChange={handleChange}
              required
            />

            <input
              type="text"
              name="phone"
              placeholder="Phone"
              value={form.phone}
              onChange={handleChange}
              required
            />

            <input
              type="text"
              name="company"
              placeholder="Company"
              value={form.company}
              onChange={handleChange}
              required
            />

            <button type="submit">+ Add Lead</button>

          </form>

        </section>

        {editingLead && (
          <section className="add-lead-section">

            <h2>Edit Lead #{editingLead.id}</h2>

            <form onSubmit={handleUpdate} className="lead-form">

              <input
                type="text"
                name="name"
                placeholder="Name"
                value={editForm.name}
                onChange={handleEditChange}
                required
              />

              <input
                type="email"
                name="email"
                placeholder="Email"
                value={editForm.email}
                onChange={handleEditChange}
                required
              />

              <input
                type="text"
                name="phone"
                placeholder="Phone"
                value={editForm.phone}
                onChange={handleEditChange}
                required
              />

              <input
                type="text"
                name="company"
                placeholder="Company"
                value={editForm.company}
                onChange={handleEditChange}
                required
              />

              <button type="submit">Save Changes</button>

              <button
                type="button"
                onClick={() => setEditingLead(null)}
              >
                Cancel
              </button>

            </form>

          </section>
        )}

        <section className="leads-section">

          <div className="section-header">
            <div>
              <h2>All Leads</h2>
              <p>Your registered contacts</p>
            </div>
          </div>

          <div className="table-container">
            <table>

              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Company</th>
                  <th>Actions</th>
                </tr>
              </thead>

              <tbody>
                {leads.map(lead => (
                  <tr key={lead.id}>
                    <td>#{lead.id}</td>
                    <td>{lead.name}</td>
                    <td>{lead.email}</td>
                    <td>{lead.phone}</td>
                    <td>{lead.company}</td>

                    <td>
                      <button onClick={() => startEdit(lead)}>
                        Edit
                      </button>

                      <button onClick={() => handleDelete(lead.id)}>
                        Delete
                      </button>
                    </td>

                  </tr>
                ))}
              </tbody>

            </table>
          </div>

        </section>

      </main>
    </div>
  );
}

export default App;
