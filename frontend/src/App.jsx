import { useState, useEffect, useRef } from 'react'

function App() {
  const [contacts, setContacts] = useState([])
  const [form, setForm] = useState({ name: '', email: '', phone: '' })
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [loadError, setLoadError] = useState('')
  const requestSeqRef = useRef(0)

  const load = async () => {
    const seq = ++requestSeqRef.current
    setLoadError('')
    try {
      const r = await fetch('http://localhost:8080/api/contacts')
      if (!r.ok) throw new Error(`Failed to load contacts (${r.status})`)
      const data = await r.json()
      if (!Array.isArray(data)) throw new Error('Invalid contacts response')
      if (seq === requestSeqRef.current) {
        setContacts(data)
      }
    } catch (err) {
      if (seq === requestSeqRef.current) {
        setLoadError(err.message || 'Unable to load contacts')
      }
    }
  }

  useEffect(() => { load() }, [])

  const updateField = (field) => (e) => {
    if (error) setError('')
    setForm({ ...form, [field]: e.target.value })
  }

  const submit = async (e) => {
    e.preventDefault()
    if (submitting) return
    setSubmitting(true)
    setError('')
    try {
      const r = await fetch('http://localhost:8080/api/contacts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      })
      if (!r.ok) throw new Error(`Failed to save contact (${r.status})`)
      setForm({ name: '', email: '', phone: '' })
      await load()
    } catch (err) {
      setError(err.message || 'Could not save contact')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div style={{ padding: 20 }}>
      <h1>Contact Management System</h1>
      <form onSubmit={submit}>
        <input placeholder="Name" value={form.name} onChange={updateField('name')} />
        <input placeholder="Email" value={form.email} onChange={updateField('email')} />
        <input placeholder="Phone" value={form.phone} onChange={updateField('phone')} />
        <button disabled={submitting}>{submitting ? 'Adding...' : 'Add Contact'}</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {loadError && (
        <p style={{ color: 'red' }}>
          {loadError} <button type="button" onClick={load}>Retry</button>
        </p>
      )}
      <ol>{contacts.map(c => <li key={c.id}>{c.name} - {c.email} - {c.phone}</li>)}</ol>
    </div>
  )
}
export default App