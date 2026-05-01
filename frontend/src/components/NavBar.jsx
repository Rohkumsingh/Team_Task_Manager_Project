import { Link, useNavigate } from 'react-router-dom'
import { getAuthUser, logout } from '../auth'

export default function NavBar() {
  const navigate = useNavigate()
  const user = getAuthUser()

  const onLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="nav">
      <div className="brand">
        <b>Task Orbit</b>
      </div>
      <div className="links">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/projects">Projects</Link>
        <Link to="/tasks">Tasks</Link>
      </div>
      <div className="user-chip">
        <span>{user?.name} ({user?.role})</span>
        <button onClick={onLogout} className="btn btn-small">Logout</button>
      </div>
    </nav>
  )
}
