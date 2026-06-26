'use client'

import { SessionProvider } from 'next-auth/react'
import { useSession, signOut } from 'next-auth/react'

const Profile = () => {
  const { data: session, status }: any = useSession()

  if (status === 'loading') {
    return <div>Loading...</div>
  }
  if (!session) {
    return (
      <div>
        <p>You are not logged in.</p>
        <a href="/login">Login</a>
      </div>
    )
  }

  return (
    <div>
      <h1>Welcome, {session.user.phone}</h1>
      <p>Email: {session.user.email}</p>
      <button onClick={() => signOut()}>Logout</button>
    </div>
  )
}

export default function ProfilePage() {
  return (
    <SessionProvider>
      <Profile />
    </SessionProvider>
  )
}
