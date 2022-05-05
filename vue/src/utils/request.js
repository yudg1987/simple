import axios from 'axios'
import router from "@/router";
//import {serverIp} from "../../public/config";

/*
 *  这里写一个读取配置文件的方法，原生ajax，同步读取文件
 */
const getConfigration = function () {
    let ajax = null;
    //判断ajax对浏览器支持情况
    if (window.XMLHttpRequest) {
        ajax = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        ajax = new window.ActiveXObject();
    }
    else {
        console.log("您的浏览器不支持ajax");
    }
    if (ajax != null) {
        ajax.open("GET", "/config.json?t=" + Date.now(), false)
        ajax.send(null);
        ajax.onreadystatechange = function () {

        };
        return JSON.parse(ajax.responseText);
    }
};

export const config = getConfigration();

const request = axios.create({
    /*baseURL: `http://${serverIp}:9091/springboot`,*/
    //baseURL: serverIp,
    baseURL: config.baseUrl,
    timeout: 30000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    if (user) {
        config.headers['token'] = user.token;  // 设置请求头
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            // res = res ? JSON.parse(res) : res
        }
        // 当权限验证不通过的时候给出提示
        if (res.code === '401') {
            // ElementUI.Message({
            //     message: res.msg,
            //     type: 'error'
            // });
            router.push("/login")
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

// 下载
export function download(query,newFileName) {
    return request({
        url: config.baseURL+'/file/download2',
        method: 'get',
        responseType: 'blob',
        params: query
    }).then((res) => {
        /**
         * blob下载思路
         * 1） 使用ajax发起请求，指定接收类型为blob（responseType: 'blob'）
         * 2）读取请求返回的头部信息里的content-disposition，返回的文件名就在这里面（或者自定义文件名，可跳过此步骤）
         * 3）使用URL.createObjectURL将请求的blob数据转为可下载的url地址
         * 4）使用a标签下载
         *
         */
        let blob = res

        // 从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
        // let patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*')
        // let contentDisposition = decodeURI(res.headers['content-disposition'])
        // let result = patt.exec(contentDisposition)
        // let fileName = result[1]


        //将请求的blob数据转为可下载的url地址
        //let url = URL.createObjectURL(blob)
        let url = window.URL.createObjectURL(blob)

        // 创建一个下载标签<a>
        const aLink = document.createElement('a')
        aLink.href = url

        // 2.直接使用自定义文件名,设置下载文件名称
        aLink.setAttribute('download', newFileName )
        document.body.appendChild(aLink)

        // 模拟点击下载
        aLink.click()

        // 移除改下载标签
        document.body.removeChild(aLink);

    })
}

