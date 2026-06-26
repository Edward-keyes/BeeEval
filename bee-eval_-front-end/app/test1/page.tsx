import { getServerSession } from "next-auth";
import { authOptions } from "../api/auth/[...nextauth]/route";

export default async function MyPage() {
    const session: any = await getServerSession(authOptions);
    if (session) {
        // console.log('登录数据'); // 打印出用户信息
    } else {
        // console.log("用户未登录");
    }
    return (
        <div>
            {session ? (
                <p>Welcome, {session.user.phone}</p>
            ) : (
                <p>Please log in.</p>
            )}
        </div>
    );
}
