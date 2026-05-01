import { useEffect, useState } from 'react'
import NavBar from '../components/NavBar'
import api from '../api'
import { getAuthUser } from '../auth'

export default function ProjectsPage() {
  const user = getAuthUser()
  const [projects, setProjects] = useState([])
  const [users, setUsers] = useState([])
  const [form, setForm] = useState({ name: '', description: '' })

  const load = async () => {
    const res = await api.get('/projects')
    setProjects(res.data)
    if (user?.role === 'ADMIN') {
      const userRes = await api.get('/users')
      setUsers(userRes.data)
    }
  }

  useEffect(() => { load() }, [])

  const createProject = async (e) => {
    e.preventDefault()
    await api.post('/projects', form)
    setForm({ name: '', description: '' })
    load()
  }

  const addMember = async (projectId, userId) => {
    await api.post(`/projects/${projectId}/members/${userId}`)
    load()
  }

  return (
    <>
      <NavBar />
      <div className="page">
        <h2>Projects</h2>

        {user?.role === 'ADMIN' && (
          <form onSubmit={createProject} className="inline-form">
            <input placeholder="Project name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            <input placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
            <button className="btn">Create</button>
          </form>
        )}

        {projects.map((p) => (
          <div key={p.id} className="list-item">
            <h4>{p.name}</h4>
            <p>{p.description}</p>
            <p><span className="muted-label">Members:</span> {p.members?.map((m) => m.name).join(', ')}</p>
            {user?.role === 'ADMIN' && (
              <select defaultValue="" onChange={(e) => e.target.value && addMember(p.id, e.target.value)}>
                <option value="">Add member</option>
                {users.map((u) => <option key={u.id} value={u.id}>{u.name}</option>)}
              </select>
            )}
          </div>
        ))}
      </div>
    </>
  )
}
