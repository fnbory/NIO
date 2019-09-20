package NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 通过selector管理通道，一个线程轮询selector，看是否有通道准备好，
 * 不阻塞主要因为有通道准备好读或者写，才会去真正的读或者写
 * @Author: fnbory
 * @Date: 2019/9/20 15:11
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8890));
        serverSocketChannel.configureBlocking(false);
        Selector selector=Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            if(selector.select()==0) continue;
            Set<SelectionKey> readkeys=selector.selectedKeys();
            //使用完毕必须移除，selector不会自己移除处理过的已选择selectionkey
            //for(SelectionKey readKey:readkeys){
            Iterator<SelectionKey> iterator=readkeys.iterator();
            while(iterator.hasNext()){
                SelectionKey readKey=iterator.next();
                iterator.remove();
                if(readKey.isAcceptable()){
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 为什么不对写感兴趣
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                else if(readKey.isReadable()){
                    // 实际情况中，可能有很多个通道都准备好了，这部分可以使用线程池
                    SocketChannel socketChannel = (SocketChannel) readKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int num = socketChannel.read(readBuffer);
                    if(num>0){
                        System.out.println("收到数据：" +new String(readBuffer.array()));
                        ByteBuffer buffer = ByteBuffer.wrap("返回给客户端的数据...".getBytes());
                        // 注意这里就能写了已经
                        socketChannel.write(buffer);
                    }
                    else if(num==-1){
                        socketChannel.close();
                    }
                }
            }
        }
    }
}
