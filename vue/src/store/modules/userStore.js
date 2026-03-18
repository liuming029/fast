import {getInfo, login, logout} from '@/api/login.js'
import {getToken, removeToken, setToken} from '@/utils/auth.js'
import defAva from '@/assets/images/profile.jpg'

const useUserStore = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            id: '',
            name: '',
            avatar: '',
        }),
        actions: {
            // 登录
            login(userInfo) {
                const username = userInfo.username.trim()
                const password = userInfo.password
                const code = userInfo.code
                const uuid = userInfo.uuid
                return new Promise((resolve, reject) => {
                    login(username, password, code, uuid).then(res => {
                        setToken(res.token)
                        this.token = res.token
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // 获取用户信息
            getInfo() {
                return new Promise((resolve, reject) => {
                    getInfo().then(res => {
                        const user = res.data
                        let avatar = user.avatar || ""
                        if (avatar.indexOf('http://') === -1 && avatar.indexOf('https://') === -1) {
                            avatar = avatar ? import.meta.env.VITE_APP_BASE_API + avatar : defAva
                        }
                        this.id = user.userId
                        this.name = user.userName
                        this.avatar = avatar
                        resolve(res)
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // 退出系统
            logOut() {
                return new Promise((resolve, reject) => {
                    logout(this.token).then(() => {
                        this.token = ''
                        removeToken()
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        }
    })

export default useUserStore
