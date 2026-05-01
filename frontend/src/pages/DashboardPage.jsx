import { useEffect, useState } from 'react'
import NavBar from '../components/NavBar'
import api from '../api'

export default function DashboardPage() {
  const [data, setData] = useState(null)

  useEffect(() => {
    api.get('/dashboard').then((res) => setData(res.data))
  }, [])

  return (
    <>
      <NavBar />
      <div className="page">
        <h2>Dashboard</h2>
        {!data ? <p>Loading...</p> : (
          <div className="cards">
            <div className="card"><h3>Total</h3><p className="metric">{data.totalTasks}</p></div>
            <div className="card"><h3>TODO</h3><p className="metric">{data.tasksByStatus.TODO}</p></div>
            <div className="card"><h3>In Progress</h3><p className="metric">{data.tasksByStatus.IN_PROGRESS}</p></div>
            <div className="card"><h3>Done</h3><p className="metric">{data.tasksByStatus.DONE}</p></div>
            <div className="card"><h3>Overdue</h3><p className="metric">{data.overdueTasks}</p></div>
          </div>
        )}
      </div>
    </>
  )
}
