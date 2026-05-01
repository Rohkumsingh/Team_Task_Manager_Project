import { useEffect, useState } from 'react'
import NavBar from '../components/NavBar'
import api from '../api'
import { getAuthUser } from '../auth'

export default function TasksPage() {
  const user = getAuthUser()
  const [projects, setProjects] = useState([])
  const [users, setUsers] = useState([])
  const [selectedProject, setSelectedProject] = useState('')
  const [tasks, setTasks] = useState([])
  const [form, setForm] = useState({ title: '', description: '', dueDate: '', priority: 'MEDIUM', assignedToUserId: '' })

  const loadProjects = async () => {
    const projectRes = await api.get('/projects')
    setProjects(projectRes.data)
    if (user?.role === 'ADMIN') {
      const userRes = await api.get('/users')
      setUsers(userRes.data)
    }
  }

  const loadTasks = async (projectId) => {
    const res = await api.get(`/tasks/project/${projectId}`)
    setTasks(res.data)
  }

  useEffect(() => { loadProjects() }, [])

  const createTask = async (e) => {
    e.preventDefault()
    await api.post(`/tasks/project/${selectedProject}`, { ...form, assignedToUserId: Number(form.assignedToUserId) })
    setForm({ title: '', description: '', dueDate: '', priority: 'MEDIUM', assignedToUserId: '' })
    loadTasks(selectedProject)
  }

  const updateStatus = async (taskId, status) => {
    await api.patch(`/tasks/${taskId}/status`, { status })
    if (selectedProject) loadTasks(selectedProject)
  }

  return (
    <>
      <NavBar />
      <div className="page">
        <h2>Tasks</h2>
        <select value={selectedProject} onChange={(e) => { setSelectedProject(e.target.value); if (e.target.value) loadTasks(e.target.value) }}>
          <option value="">Select project</option>
          {projects.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
        </select>

        {user?.role === 'ADMIN' && selectedProject && (
          <form onSubmit={createTask} className="inline-form">
            <input placeholder="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
            <input placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
            <input type="date" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })} />
            <select value={form.priority} onChange={(e) => setForm({ ...form, priority: e.target.value })}>
              <option>LOW</option><option>MEDIUM</option><option>HIGH</option>
            </select>
            <select value={form.assignedToUserId} onChange={(e) => setForm({ ...form, assignedToUserId: e.target.value })}>
              <option value="">Assign user</option>
              {users.map((u) => <option key={u.id} value={u.id}>{u.name}</option>)}
            </select>
            <button className="btn">Create Task</button>
          </form>
        )}

        {tasks.map((t) => (
          <div key={t.id} className="list-item">
            <h4>{t.title} <span className={`badge priority-${t.priority}`}>{t.priority}</span></h4>
            <p>{t.description}</p>
            <p><span className="muted-label">Assigned to:</span> {t.assignedTo?.name}</p>
            <p><span className="muted-label">Due:</span> {t.dueDate || '-'}</p>
            <p><span className="muted-label">Status:</span> <span className={`badge status-${t.status}`}>{t.status}</span></p>
            {user?.role === 'MEMBER' ? (
              <select value={t.status} onChange={(e) => updateStatus(t.id, e.target.value)}>
                <option>TODO</option><option>IN_PROGRESS</option><option>DONE</option>
              </select>
            ) : null}
          </div>
        ))}
      </div>
    </>
  )
}
