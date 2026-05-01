export const getAuthUser = () => {
  const raw = localStorage.getItem('user')
  return raw ? JSON.parse(raw) : null
}

export const isLoggedIn = () => !!localStorage.getItem('token')

export const saveAuth = (data) => {
  localStorage.setItem('token', data.token)
  localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email, role: data.role }))
}

export const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}
