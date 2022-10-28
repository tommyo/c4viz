import { Client } from '@stomp/stompjs'

export let connected = false

const stompClient = new Client({
  brokerURL: 'ws://localhost:9000/greeting',
  debug: console.log
})

stompClient.onDisconnect = () => {
  connected = false
}

stompClient.onConnect = (frame) => {
  console.log({ frame });
  connected = true

  // stompClient.subscribe('/ws/list', (msg) => {
  //   console.log('/ws/list', JSON.parse(msg.body))
  // })

  stompClient.subscribe('/topic/list', (msg) => {
    console.log('/topic/list', JSON.parse(msg.body))
  })

  stompClient.subscribe('/topic/error', (error) => {
    console.log({ error })
  })

  stompClient.publish({ destination: '/ws/list' })
  // setInterval(() => {
  //   stompClient.publish({ destination: '/ws/list' })
  // }, 5000)
}

stompClient.onStompError = (err) => {
  console.log({ err })
  connected = false
}
export function connect() {
  stompClient.activate()
}

export function disconnect() {
  stompClient.deactivate()
}
