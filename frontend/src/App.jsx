import { useState, useEffect } from 'react'

function App() {
  const [contacts, setContacts] = useState([])
  const [form, setForm] = useState({ name: '', email: '', phone: '' })

  const load = () => fetch('http://localhost:8080/api/contacts').then(r => r.json()).then(setContacts)
  useEffect(() => { load() }, [])

  const submit = async (e) => {
    e.preventDefault()
    await fetch('http://localhost:8080/api/contacts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })
    setForm({ name: '', email: '', phone: '' })
    load()
  }

  return (
    <div style={{ padding: 20 }}>
      <h1>Contact Management System</h1>
      <form onSubmit={submit}>
        <input placeholder="Name" value={form.name} onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Email" value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
        <input placeholder="Phone" value={form.phone} onChange={e => setForm({...form, phone: e.target.value})} />
        <button>Add Contact</button>
      </form>
      <ol>{contacts.map(c => <li key={c.id}>{c.name} - {c.email} - {c.phone}</li>)}</ol>
    </div>
  )
}
export default App